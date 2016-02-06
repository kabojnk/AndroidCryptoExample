package poitot.com.cryptoexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import poitot.com.cryptoexample.utils.ClipboardUtils;
import poitot.com.cryptoexample.utils.CrytoUtils;
import poitot.com.cryptoexample.utils.StringUtils;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String CRYPTO_PASSWORD = "qbeGK9LtZFcCB3";

    private EditText mEtInput;
    private TextView mTvOutput;
    private Button mBtnEncrypt;
    private Button mBtnDecrypt;
    private Button mBtnCopy;
    private Button mBtnClear;
    private Button mBtnPaste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mEtInput = (EditText) findViewById(R.id.etInput);
        mTvOutput = (TextView) findViewById(R.id.tvOutput);
        mBtnEncrypt = (Button) findViewById(R.id.btnEncrypt);
        mBtnDecrypt = (Button) findViewById(R.id.btnDecrypt);
        mBtnCopy = (Button) findViewById(R.id.btnCopy);
        mBtnClear = (Button) findViewById(R.id.btnClear);
        mBtnPaste = (Button) findViewById(R.id.btnPaste);
    }

    private void initEvents() {

        // Encrypt text
        mBtnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String plainText = mEtInput.getText().toString();
                if (StringUtils.isNullOrEmpty(plainText)) {
                    Toast.makeText(MainActivity.this, "Cannot encrypt empty text. Please enter something in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    final String encryptedText = CrytoUtils.encryptText(CRYPTO_PASSWORD.toCharArray(), plainText);
                    mTvOutput.setText(encryptedText);
                } catch (Exception e) {
                    Timber.e(e, "mBtnEncrypt.onClick() -- Could not encrypt text");
                }
            }
        });

        // Decrypt text
        mBtnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String encryptedText = mEtInput.getText().toString();
                if (StringUtils.isNullOrEmpty(encryptedText)) {
                    Toast.makeText(MainActivity.this, "Cannot decrypt empty text. Please enter something in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    final String decrypedText = CrytoUtils.decryptData(CRYPTO_PASSWORD.toCharArray(), encryptedText);
                    mTvOutput.setText(decrypedText);
                } catch (Exception e) {
                    Timber.e(e, "mBtnDecrypt.onClick() -- Could not decrypt text");
                }
            }
        });

        // Copy output to clipboard
        mBtnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(MainActivity.this, mTvOutput.getText().toString());
            }
        });

        // Paste clipboard to input
        mBtnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String clipboardText = ClipboardUtils.getTextFromClipboard(MainActivity.this);
                mEtInput.setText(clipboardText);
            }
        });

        // Clear input/output
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtInput.setText("");
                mTvOutput.setText(getString(R.string.output_example));
            }
        });
    }
}
