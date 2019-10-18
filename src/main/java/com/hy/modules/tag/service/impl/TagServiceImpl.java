package com.hy.modules.tag.service.impl;

import com.hy.common.base.service.impl.BaseServiceImpl;
import com.hy.modules.tag.entity.Tag;
import com.hy.modules.tag.mapper.TagMapper;
import com.hy.modules.tag.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends BaseServiceImpl<TagMapper, Tag> implements TagService {
}
