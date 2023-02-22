package com.vgroyalchemist.controller;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class CustomFragmentActivity extends AppCompatActivity {

	public abstract void replaceFragment(Fragment fragment, boolean addToBackStack, int transition, String stringTag);
	public abstract void addFragment(Fragment fragment, boolean addToBackStack, int transition, String stringTag);
	public abstract void addFragmentWithoutAnimation(Fragment fragment, boolean addToBackStack, int transition, String stringTag);
	public abstract void removeFragment(Fragment fragment, String stringTag);

}