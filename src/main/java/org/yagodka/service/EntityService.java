package org.yagodka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yagodka.model.EntitiesResponse;
import org.yagodka.model.EntityRequest;
import org.yagodka.model.EntityResponse;
import org.yagodka.model.MyEntity;
import org.yagodka.repository.EntityRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    @Autowired
    private EntityRepository entityRepository;

    public List<EntitiesResponse> getEntities(String filter, String order) {
        List<MyEntity> entities;

        if (order == null || order.trim().isEmpty()) {
            order = "ASC";
        }

        if (filter != null && !filter.trim().isEmpty()) {
            entities = entityRepository.findByFilterWithOrder(filter.trim(), order);
        } else {
            entities = entityRepository.findAllWithOrder(order);
        }

        return entities.stream()
                .map(EntitiesResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public EntityResponse createEntity(EntityRequest request) {
        String currentDateTime = getCurrentDateTime();
        Long userId = getUserId();

        MyEntity myEntity = new MyEntity();
        myEntity.setType(request.getType());
        myEntity.setDisplayName(request.getDisplayName());
        myEntity.setMinus(request.getMinus() != null ? request.getMinus() : null);
        myEntity.setPlus(request.getPlus() != null ? request.getPlus() : null);
        myEntity.setDescription(request.getDescription() != null ? request.getDescription() : null);
        myEntity.setEstimation(request.getEstimation());
        myEntity.setImage(request.getImage() != null ? request.getImage() : null);
        myEntity.setIsFavourite(request.getIsFavourite() != null ? request.getIsFavourite() : null);
        myEntity.setByUser(userId);
        myEntity.setAddDate(currentDateTime);
        myEntity.setUpdateDate(currentDateTime);

        MyEntity savedMyEntity = entityRepository.save(myEntity);
        return EntityResponse.fromEntity(savedMyEntity);
    }

    public EntityResponse updateEntity(Long id, EntityRequest request) {
        MyEntity existingMyEntity = entityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity не найден"));

        if (request.getType() != null) {
            existingMyEntity.setType(request.getType());
        }
        if (request.getDisplayName() != null) {
            existingMyEntity.setDisplayName(request.getDisplayName());
        }
        if (request.getMinus() != null) {
            existingMyEntity.setMinus(request.getMinus());
        }
        if (request.getPlus() != null) {
            existingMyEntity.setPlus(request.getPlus());
        }
        if (request.getDescription() != null) {
            existingMyEntity.setDescription(request.getDescription());
        }
        if (request.getEstimation() != null) {
            existingMyEntity.setEstimation(request.getEstimation());
        }
        if (request.getImage() != null) {
            existingMyEntity.setImage(request.getImage());
        }
        if (request.getIsFavourite() != null) {
            existingMyEntity.setIsFavourite(request.getIsFavourite());
        }

        existingMyEntity.setUpdateDate(getCurrentDateTime());

        MyEntity updatedMyEntity = entityRepository.save(existingMyEntity);
        return EntityResponse.fromEntity(updatedMyEntity);
    }


    public EntityResponse getEntityById(Long id) {
        MyEntity existingDog = entityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity не найден"));

        return EntityResponse.fromEntity(existingDog);
    }


    public void deleteEntity(Long id) {
        if (!entityRepository.existsById(id)) {
            throw new RuntimeException("Entity не найден");
        }
        entityRepository.deleteById(id);
    }

    private String getCurrentDateTime() {
        return ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .format(DATE_FORMATTER);
    }

    private Long getUserId() {
        return 0L;
    }
}
