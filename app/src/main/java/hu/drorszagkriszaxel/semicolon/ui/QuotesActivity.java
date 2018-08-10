package hu.drorszagkriszaxel.semicolon.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.ui.adapter.QuotePagerAdapter;

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
 * This class provides QuotesActivity's functionality
 *
 */
public class QuotesActivity extends AppCompatActivity {

    @BindView(R.id.quote_pager) ViewPager viewPager;

    /**
     * Creates the activity's instances.
     *
     * @param savedInstanceState Instances state if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        setSupportActionBar((Toolbar) findViewById(R.id.quote_toolbar));

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final QuotePagerAdapter quotePagerAdapter = new QuotePagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.tab_labels_quote));

        viewPager.setAdapter(quotePagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * Helps the side fragments to keep their freshness.
             *
             * @param position Position to determine whether help is needed
             */
            @Override
            public void onPageSelected(int position) {

                if (position == 0 || position == 2) quotePagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * Handles back button press to go back to parent.
     */
    @Override
    public void onBackPressed() {

        finish();

    }
}
