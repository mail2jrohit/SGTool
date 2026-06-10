package com.rssh.saintgobaintool.repository;

import com.rssh.saintgobaintool.entity.RowEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRecordRepository extends JpaRepository<RowEntry, Integer> {

}
