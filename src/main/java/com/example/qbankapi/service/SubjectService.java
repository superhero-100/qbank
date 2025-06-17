package com.example.qbankapi.service;

import com.example.qbankapi.dao.SubjectDao;
import com.example.qbankapi.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectDao subjectDao;

    public List<Subject> getAll() {
        return subjectDao.findAll();
    }

    public void addAll(List<String> subjectList) {
        subjectDao.saveAll(subjectList);
    }

}
