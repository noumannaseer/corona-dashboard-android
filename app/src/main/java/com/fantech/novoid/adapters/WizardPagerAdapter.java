package com.fantech.novoid.adapters;


import com.fantech.novoid.fragments.Wizard1Fragment;
import com.fantech.novoid.fragments.Wizard2Fragment;
import com.fantech.novoid.fragments.Wizard3Fragment;
import com.fantech.novoid.fragments.Wizard4Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//**********************************************
public class WizardPagerAdapter
        extends FragmentPagerAdapter
//**********************************************
{

    final int PAGE_COUNT = 4;

    //**********************************************
    public WizardPagerAdapter(@NonNull FragmentManager fm, int behavior)
    //**********************************************
    {
        super(fm, behavior);
    }

    //**********************************************
    @Override
    public Fragment getItem(int arg0)
    //**********************************************
    {
        switch (arg0)
        {
        case 0:
            return new Wizard1Fragment();
        case 1:
            return new Wizard2Fragment();
        case 2:
            return new Wizard3Fragment();
        case 3:
            return new Wizard4Fragment();
        default:
            return null;
        }

    }

    //**********************************************
    @Override
    public int getCount()
    //**********************************************
    {
        return PAGE_COUNT;
    }

}