package com.example.dogwalk;

import android.graphics.Bitmap;

/**
 * This is a very simple interface that allows the passing of the map screenshot and information to the
 * PreviousRoute fragment.
 */
public interface mapInterface
{
    void sendSnapshot(Bitmap snapshot, String info);
}