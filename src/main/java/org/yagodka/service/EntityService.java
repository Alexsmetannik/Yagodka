package org.yagodka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yagodka.model.MyEntity;
import org.yagodka.model.EntityRequest;
import org.yagodka.model.EntityResponse;
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

    public List<EntityResponse> getEntities(String filter, String order) {
        List<MyEntity> entities;

        if (filter != null && !filter.trim().isEmpty()) {
            if ("DESC".equalsIgnoreCase(order)) {
                entities = entityRepository.findByFilterWithDescOrder(filter);
            } else {
                entities = entityRepository.findByFilterWithDefaultOrder(filter);
            }
        } else {
            if ("DESC".equalsIgnoreCase(order)) {
                entities = entityRepository.findAllWithDescOrder();
            } else {
                entities = entityRepository.findAllWithDefaultOrder();
            }
        }

        return entities.stream()
                .map(EntityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public EntityResponse createEntity(EntityRequest request) {
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
        return EntityResponse.fromEntity(savedMyEntity);
    }

    public EntityResponse updateEntity(Long id, EntityRequest request) {
        MyEntity existingMyEntity = entityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));

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
        return EntityResponse.fromEntity(updatedMyEntity);
    }

    public void deleteEntity(Long id) {
        if (!entityRepository.existsById(id)) {
            throw new RuntimeException("Entity not found");
        }
        entityRepository.deleteById(id);
    }

    public MyEntity findEntityById(Long id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    private String getCurrentDateTime() {
        return ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .format(DATE_FORMATTER);
    }
}
