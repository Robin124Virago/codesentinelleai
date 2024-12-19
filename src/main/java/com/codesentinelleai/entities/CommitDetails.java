package com.codesentinelleai.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class CommitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String branch;
    private String repoName;
    private String commitId;
    private String message;

    @ElementCollection
    private List<String> addedFiles;

    @ElementCollection
    private List<String> modifiedFiles;

    @ElementCollection
    private List<String> removedFiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAddedFiles() {
        return addedFiles;
    }

    public void setAddedFiles(List<String> addedFiles) {
        this.addedFiles = addedFiles;
    }

    public List<String> getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(List<String> modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

    public List<String> getRemovedFiles() {
        return removedFiles;
    }

    public void setRemovedFiles(List<String> removedFiles) {
        this.removedFiles = removedFiles;
    }
}
