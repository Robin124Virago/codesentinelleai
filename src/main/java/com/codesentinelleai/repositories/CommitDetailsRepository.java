package com.codesentinelleai.repositories;

import com.codesentinelleai.entities.CommitDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitDetailsRepository extends JpaRepository<CommitDetails, Long> {
}
