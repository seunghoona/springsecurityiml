package com.inflearn.springsecurityiml.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ROLE_HIERARCHY")
public class RolesHierarchy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "child_name")
    private String childName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "child_name")
    private RolesHierarchy parentName;

    @OneToMany(mappedBy = "parentName", cascade = CascadeType.ALL)
    private Set<RolesHierarchy> roleHierarchies = new HashSet<>();

}
