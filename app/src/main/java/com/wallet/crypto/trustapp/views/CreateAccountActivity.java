package com.wallet.crypto.trustapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.wallet.crypto.trustapp.R;
import com.wallet.crypto.trustapp.controller.Controller;
import com.wallet.crypto.trustapp.controller.ServiceErrorException;
import com.wallet.crypto.trustapp.util.PasswordStoreFactory;

/**
 * A login screen that offers login via email/password.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private Controller mController;

    // Constant
    private static final int MIN_PASSWORD_LENGTH = 4;

    // UI references.
    private Button mImportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ActionBar actionBar = getSupportActionBar();

        mController = Controller.with(this);

        if (mController.getNumberOfAccounts() == 0) {
            showIntro();
        }

        if (actionBar != null && mController.getCurrentAccount() != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button mCreateAccountButton = findViewById(R.id.create_account_button);
        mCreateAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateAccount();
            }
        });

        mImportButton = findViewById(R.id.import_account_button);
        mImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.navigateToImportAccount(CreateAccountActivity.this);
            }
        });
    }

	private void onCreateAccount() {
		final String generatedPassphrase = PasswordStoreFactory.generatePassword();
		try {
			mController.clickCreateAccount(CreateAccountActivity.this, "account name", generatedPassphrase);
		} catch (Exception e) {
			Log.d("CREATE_ACC", "Error", e);
			if (e instanceof ServiceErrorException
					&& ((ServiceErrorException) e).code == ServiceErrorException.USER_NOT_AUTHENTICATED) {
				PasswordStoreFactory.showAuthenticationScreen(CreateAccountActivity.this, Controller.UNLOCK_SCREEN_REQUEST);
			} else {
				Toast.makeText(getApplicationContext(), "Create account: " + e.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}

	private void showIntro() {
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //  If the activity has never started before...
                if (mController.getAccounts().size() == 0) {

                    //  Launch app intro
                    final Intent i = new Intent(CreateAccountActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });
                }
            }
        });

        // Start the thread
        t.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Controller.IMPORT_ACCOUNT_REQUEST) {
            if (resultCode == RESULT_OK) {
                boolean firstAccount = mController.getAccounts().size() == 1;
                if (firstAccount) {
                    mController.setCurrentAddress(mController.getAccounts().get(0).getAddress());
                    Intent intent = new Intent(this.getApplicationContext(), TransactionListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.getApplicationContext().startActivity(intent);
                }
                this.finish();
            }
        } else if (requestCode == Controller.UNLOCK_SCREEN_REQUEST) {
        	if (resultCode == RESULT_OK) {
        		onCreateAccount();
	        } else {
        		finish();
	        }
        }
    }

    private boolean isPasswordLongEnough(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

