package com.kzk.kmall.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the SystemNotice entity. This class is used in SystemNoticeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /system-notices?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemNoticeCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter content;

    private StringFilter title;

    private LongFilter sendToId;

    public SystemNoticeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public LongFilter getSendToId() {
        return sendToId;
    }

    public void setSendToId(LongFilter sendToId) {
        this.sendToId = sendToId;
    }

    @Override
    public String toString() {
        return "SystemNoticeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (sendToId != null ? "sendToId=" + sendToId + ", " : "") +
            "}";
    }

}
