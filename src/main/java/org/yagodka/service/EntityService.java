package org.yagodka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yagodka.model.EntitiesRequest;
import org.yagodka.model.EntitiesResponse;
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

    public EntitiesResponse createEntity(EntitiesRequest request) {
        String currentDateTime = getCurrentDateTime();

        MyEntity myEntity = new MyEntity();
        myEntity.setType(request.getType());
        myEntity.setDisplayName(request.getDisplayName());
        myEntity.setDescription(request.getDescription());
        myEntity.setEstimation(request.getEstimation());
        myEntity.setImage(request.getImage());
        myEntity.setAddDate(currentDateTime);
        myEntity.setUpdateDate(currentDateTime);

        MyEntity savedMyEntity = entityRepository.save(myEntity);
        return EntitiesResponse.fromEntity(savedMyEntity);
    }

    public EntitiesResponse updateEntity(Long id, EntitiesRequest request) {
        MyEntity existingMyEntity = entityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity не найден"));

        if (request.getType() != null) {
            existingMyEntity.setType(request.getType());
        }
        if (request.getDisplayName() != null) {
            existingMyEntity.setDisplayName(request.getDisplayName());
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

        existingMyEntity.setUpdateDate(getCurrentDateTime());

        MyEntity updatedMyEntity = entityRepository.save(existingMyEntity);
        return EntitiesResponse.fromEntity(updatedMyEntity);
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
}
