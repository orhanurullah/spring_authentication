package com.ortim.model;

import com.ortim.core.utils.DbConstants;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@MappedSuperclass
@Getter
@Setter
public class BaseModel extends BaseIdModel {


	@Column(name = DbConstants.tableUpdatedDate, nullable = false)
	private LocalDateTime updatedDate = LocalDateTime.now(ZoneOffset.of(DbConstants.offSet));;

	@Column(name = DbConstants.tableCreatedDate, nullable = false)
	private LocalDateTime createdDate = LocalDateTime.now(ZoneOffset.of(DbConstants.offSet));;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseModel other = (BaseModel) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (updatedDate == null) {
            return other.updatedDate == null;
		} else return updatedDate.equals(other.updatedDate);
    }

}