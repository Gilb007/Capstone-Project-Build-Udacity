package com.example.vladkolomysov.capstone.data;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by vladkolomysov
 */

// for sql
@SimpleSQLTable(
        table = "Favourites",
        provider = "FavouritesProvider")

public class FavouriteANDataBaseHelper {

    @SimpleSQLColumn(FavouriteANContract.COLUMN_AN_AUTHOR)
    public String author;

    @SimpleSQLColumn(FavouriteANContract.COLUMN_AN_IMAGE)
    public String image;

    @SimpleSQLColumn(FavouriteANContract.COLUMN_AN_DATE)
    public String date;

    @SimpleSQLColumn(value = FavouriteANContract.COLUMN_AN_URL, primary = true)
    public String url;

    @SimpleSQLColumn(FavouriteANContract.COLUMN_AN_TITLE)
    public String title;

    @SimpleSQLColumn(FavouriteANContract.COLUMN_AN_DESCRIPTION)
    public String description;

}