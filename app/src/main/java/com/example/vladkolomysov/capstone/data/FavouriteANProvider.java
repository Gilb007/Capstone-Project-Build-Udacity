package com.example.vladkolomysov.capstone.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by vladkolomysov
 */

// for sql
@SimpleSQLConfig(
        name = "FavouritesProvider",
        authority = "com.example.vladkolomysov.capstone",
        database = "favourites.db",
        version = 1)

public class FavouriteANProvider implements ProviderConfig {

    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }

}