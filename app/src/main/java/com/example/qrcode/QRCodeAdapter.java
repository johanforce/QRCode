package com.example.qrcode;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class QRCodeAdapter extends FragmentStatePagerAdapter {

    public QRCodeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new Frg_CreateQR();
                break;
            case 1:
                frag = new Frg_ScanQR();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Tạo mã QR";
                break;
            case 1:
                title = "Quét mã QR";
                break;
        }
        return title;
    }
}
