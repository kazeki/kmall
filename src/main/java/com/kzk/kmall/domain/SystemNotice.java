package com.kzk.kmall.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SystemNotice.
 */
@Entity
@Table(name = "system_notice")
@Document(indexName = "systemnotice")
public class SystemNotice extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @NotNull
    @Size(max = 20)
    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @ManyToOne
    private User sendTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public SystemNotice content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public SystemNotice title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getSendTo() {
        return sendTo;
    }

    public SystemNotice sendTo(User user) {
        this.sendTo = user;
        return this;
    }

    public void setSendTo(User user) {
        this.sendTo = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemNotice systemNotice = (SystemNotice) o;
        if (systemNotice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemNotice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemNotice{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
