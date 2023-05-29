package com.example.MySQL_Practicce.repository;

import com.example.MySQL_Practicce.domain.Member;

public interface RepositoryException {
    Member save(Member member);
    Member findById(String memberId);
    void update(String memberId, int money);
    void delete(String memberId);
}
