package com.hy.modules.tag.entity;

import com.hy.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private int id;
	private int topFlag;
	private String name;

}
