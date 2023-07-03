package mz.co.attendance.control.dao.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class PageMixin<T> {

    @JsonCreator
    public PageMixin(@JsonProperty("content") List<T> content,
                     @JsonProperty("pageable") Pageable pageable,
                     @JsonProperty("totalElements") long totalElements) {
        new PageImpl<>(content, pageable, totalElements);
    }


}
