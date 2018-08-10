package hu.drorszagkriszaxel.semicolon.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hu.drorszagkriszaxel.semicolon.ui.fragment.CodeListFragment;

/**
 *
 * SemiColon
 *
 * © 2018 by Axel Ország-Krisz Dr.
 *
 * @author  Axel Ország-Krisz Dr.
 * @version 1.0 - first try
 *
 * ---
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---
 *
 * For more information and source check out:
 *
 * https://github.com/okaxel/udacitySemiColon
 *
 * ---
 *
 * Adapter class to handle connection between Code List Activity's ViewPager and Fragments
 *
 */
public class CodePagerAdapter extends FragmentPagerAdapter {

    private final static int pageCount = 3;
    private String[] tabNames;

    /**
     * Classical constructor. Sets the name of fragments.
     *
     * @param fm       FragmentManager to give it to super class
     * @param tabNames Names of tabs
     */
    public CodePagerAdapter(FragmentManager fm, String[] tabNames) {

        super(fm);

        if (tabNames.length == 3) this.tabNames = tabNames;
        else tabNames = new  String[] {"1","2","3"};

    }

    /**
     * Gets a fragment instance.
     *
     * @param position Position
     * @return         Fragment
     */
    @Override
    public Fragment getItem(int position) {

        return CodeListFragment.newInstance(position);

    }

    /**
     * Gets count of pages.
     *
     * @return Count of pages
     */
    @Override
    public int getCount() {

        return pageCount;

    }

    /**
     * Gets the name of rhs selected screen.
     *
     * @param position Position
     * @return         Name
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return tabNames[position];

    }

}
