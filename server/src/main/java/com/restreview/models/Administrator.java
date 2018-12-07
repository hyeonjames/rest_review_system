package com.restreview.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.restreview.etc.UserType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(UserType.ADMINISTRATOR)
public class Administrator extends User {
}
