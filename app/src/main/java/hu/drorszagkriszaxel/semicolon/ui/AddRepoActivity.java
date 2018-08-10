package hu.drorszagkriszaxel.semicolon.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Code;

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
 * This class provides AddRepoActivity's functionality
 *
 */
public class AddRepoActivity extends AppCompatActivity {

    @BindView(R.id.add_repo_coordinator) CoordinatorLayout coordinatorLayout;

    @BindView(R.id.et_add_repo_owner) EditText etOwner;
    @BindView(R.id.et_add_repo_name) EditText etName;
    @BindView(R.id.et_add_repo_alias) EditText etAlias;

    @BindView(R.id.add_repo_input_result) TextView tvResult;

    @BindView(R.id.btn_add_repo_add) Button btnAdd;
    @BindView(R.id.btn_add_repo_no) Button btnNo;

    private String rOwner = "";
    private String rName = "";
    private String rAlias = "";

    /**
     * Creates the activity's instances.
     *
     * @param savedInstanceState Instances state if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_repo);

        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.add_repo_toolbar));

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        etOwner.addTextChangedListener(new RepoTextWatcher());
        etName.addTextChangedListener(new RepoTextWatcher());
        etAlias.addTextChangedListener(new RepoTextWatcher());

        tvResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isValid()) startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.github_helper,rOwner,rName))));

            }

        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isValid()) {

                    LocalData.updateCode(
                            getContentResolver(),new Code(Code.CODE_REPO,rAlias,rName,rOwner));

                    finish();

                }

            }

        });

        btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();

            }

        });

    }

    /**
     * Determines whether two required EditTexts are empty or not.
     *
     * @return true or false
     */
    boolean isValid() {

        return (!rOwner.equals("")) && (!rName.equals(""));

    }

    /**
     * Updates the link created from the texts added by the user.
     */
    void updateResult() {

        rOwner = etOwner.getText().toString();
        rName = etName.getText().toString();
        rAlias = etAlias.getText().toString();

        if (isValid()) tvResult.setText(getString(R.string.github_helper,rOwner,rName));
        else tvResult.setText("");

    }

    /**
     * TextWatcher class to watch when text changed.
     */
    class RepoTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        /**
         * Handles text changes.
         *
         * @param charSequence Required but not used
         * @param i            Required but not used
         * @param i1           Required but not used
         * @param i2           Required but not used
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            updateResult();

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

    }

}
