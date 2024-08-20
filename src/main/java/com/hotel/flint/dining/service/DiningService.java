package com.hotel.flint.dining.service;

import com.hotel.flint.common.enumdir.DiningName;
import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.dining.domain.Menu;
import com.hotel.flint.dining.dto.MenuSaveDto;
import com.hotel.flint.dining.repository.DiningRepository;
import com.hotel.flint.dining.repository.MenuRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class DiningService {
    private final DiningRepository diningRepository;
    private final MenuRepository menuRepository;

    public DiningService(DiningRepository diningRepository, MenuRepository menuRepository){
        this.diningRepository = diningRepository;
        this.menuRepository = menuRepository;
    }

    public void addDiningMenu(MenuSaveDto menuSaveDto){
        Dining dining = diningRepository.findById(menuSaveDto.getDiningId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Dining ID"));

        Menu menu = menuSaveDto.toEntity(dining);
        menuRepository.save(menu);
    }

    public void modDiningMenu(Long id, int newCost){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 메뉴"));
        menu.menuUpdate(newCost);
    }

    public void delDiningMenu(Long id){
        if(menuRepository.findById(id).isPresent()){
            menuRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException("존재하지 않는 메뉴");
        }
    }

    public DiningName getName(Long id){
        Dining dining = diningRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 다이닝"));
        return dining.getDiningName();
    }
}