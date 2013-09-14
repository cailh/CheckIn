package com.checkin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

public class LoginActivity extends Activity {

	private String username = null;
	private String password = null;

	private Button login_btn, regist_btn, wifi_set_btn;
	private EditText account_edt;
	private EditText psw_edt;
	private SocketUtil connect;

	static boolean hasLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		if(new PreferGeter(this).getIP().equalsIgnoreCase("NULL")){
			Toast.makeText(this,"��δ���ù�˾wifi��Ϣ,��������",Toast.LENGTH_LONG).show();
		}

		account_edt = (EditText) this.findViewById(R.id.login_edit_account);
		psw_edt = (EditText) this.findViewById(R.id.login_edit_pwd);
		login_btn = (Button) this.findViewById(R.id.login_cb_savepwd);
		regist_btn = (Button) this.findViewById(R.id.regist);
		wifi_set_btn = (Button) this.findViewById(R.id.baseset);

		login_btn.setOnClickListener(new OnClickListener() { // ��¼

					@Override
					public void onClick(View v) {

						connect = new SocketUtil(LoginActivity.this);
						if (LoginActivity.this.getCurrentFocus() != null) {
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											LoginActivity.this
													.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
						}
						if (!connect.isConnected) {
							try {
								connect.connectServer();
							} catch (Exception e) {
								Toast.makeText(LoginActivity.this,
										"���ӷ�����ʧ�ܣ���������״��..", Toast.LENGTH_LONG)
										.show();
								return;
							}
						}
						username = account_edt.getText().toString();
						password = psw_edt.getText().toString();
						if (username.trim().length() == 0
								&& password.trim().length() == 0) {
							Toast.makeText(LoginActivity.this, "id�����벻��Ϊ��^^",
									Toast.LENGTH_SHORT).show();
							return;
						}
						boolean isValid = connect.loginValidate(username,
								password);
						connect.close();
						if (isValid) {

							hasLogin = true;
							Toast.makeText(LoginActivity.this, "��¼�ɹ�^^",
									Toast.LENGTH_SHORT).show();
							// �޸ļ�ס����preference
							// �����û���������sharedPreference��ΪKey ID
							Editor editor;
							editor = LoginActivity.this.getSharedPreferences(
									"user", Context.MODE_PRIVATE).edit();
							editor.putString("username", username);
							editor.putString("password", password);
							editor.commit();
							Intent intent0 = new Intent(LoginActivity.this,
									MainActivity.class);
							startActivity(intent0);

						} else {
							Toast.makeText(LoginActivity.this, "��¼ʧ�ܣ�",
									Toast.LENGTH_SHORT).show();
						}
					}

				});

		regist_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this, RegistActivity.class));
			}
			
		});

		wifi_set_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						Setting.class));
			}
		});

	}

}