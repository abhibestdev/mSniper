package me.abhi.sniper.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Account {

    @Getter private final String email;
    @Getter private final String password;
    @Setter @Getter private final String bearerToken;
    @Setter @Getter private final String uuid;
}
