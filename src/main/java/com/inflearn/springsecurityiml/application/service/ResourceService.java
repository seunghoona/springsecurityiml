package com.inflearn.springsecurityiml.application.service;

import com.inflearn.springsecurityiml.domain.Resources;
import java.util.List;

public interface ResourceService {
    Resources selectResources(long id);

    List<Resources> selectResources();

    void insertResources(Resources Resources);

    void deleteResources(long id);
}
