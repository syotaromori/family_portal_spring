package com.iromoratoys.family_portal.growth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/growth-records")
@CrossOrigin
public class GrowthRecordController {

    private final GrowthRecordService service;

    public GrowthRecordController(GrowthRecordService service) {
        this.service = service;
    }

    // 一覧(子どもID指定)
    @GetMapping
    public List<GrowthRecordResponse> getAll(@RequestParam Long childId) {
        return service.findAll(childId)
                .stream()
                .map(GrowthRecordResponse::new)
                .toList();
    }

    // 登録
    @PostMapping
    public GrowthRecordResponse create(@Valid @RequestBody GrowthRecordRequest req) {
        return new GrowthRecordResponse(service.create(req));
    }

    // 更新
    @PutMapping("/{id}")
    public GrowthRecordResponse update(@PathVariable Long id,
                                        @Valid @RequestBody GrowthRecordUpdateRequest req) {
        return new GrowthRecordResponse(service.update(id, req));
    }

    @GetMapping("/{id}")
    public GrowthRecordResponse getById(@PathVariable Long id) {
        return new GrowthRecordResponse(service.getById(id));
    }

    // 削除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
