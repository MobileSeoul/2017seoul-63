package com.noti.sns.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.noti.sns.R
import com.noti.sns.server.Connection
import com.noti.sns.utility.BtnPress
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //초기선언
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //확인 버튼 클릭시 팝업 뒤쪽 못건드리게
        register_confirm.setOnClickListener {
            pin_popup.visibility = View.VISIBLE
            reg_email.isEnabled = false
            reg_name.isEnabled = false
            reg_password.isEnabled = false
            reg_school.isEnabled = false
            reg_schnum.isEnabled = false
            sign_up_back.isEnabled = false
            register_confirm.isEnabled = false


        }

        //버튼 클릭 애니메이션
        register_confirm.setOnTouchListener({ _, motionEvent ->
            BtnPress.bigBTN(motionEvent, register_confirm)
        })

        //방 이름 확인 누를때
        pin_btn.setOnClickListener {
            if (roomNameEdit.text.equals(""))
                Toast.makeText(this, "방 이름이 빈칸입니다.", Toast.LENGTH_SHORT).show()
            else {
                register(reg_email.text, reg_password.text, reg_schnum.text, reg_name.text, roomNameEdit.text, reg_school.text)
            }
        }

        //버튼 클릭 애니메이션
        pin_btn.setOnTouchListener({ _, motionEvent ->
            BtnPress.pinBTN(motionEvent, pin_btn)
        })


    }

    //뒤로가기 키 설정
    override fun onBackPressed() {
        if (pin_popup.visibility == View.VISIBLE) {
            //팝업이 있을때 처리
            pin_popup.visibility = View.GONE
            reg_email.isEnabled = true
            reg_name.isEnabled = true
            reg_password.isEnabled = true
            reg_school.isEnabled = true
            reg_schnum.isEnabled = true
            register_confirm.isEnabled = true
        } else {
            super.onBackPressed()
        }
    }

    fun register(email: Editable, password: Editable, stnumber: Editable, name: Editable, room: Editable, school: Editable) {
        var response: Array<String>
        /*
         * 반환값 실제 값은 각각 response[1] / response[3]으로 접근할 수 있음
         * response,register_failed:nonexistent_room 방 없음
         * response,register_failed:existent_account 이미 있음
         * response,register_success 회원가입 성공 ==> 이메일 인증하라고 하고, 로그인창 띄어줘야함
         */
        val js = JSONObject()
        js.put("email", email)
        js.put("password", password)
        js.put("stnumber", stnumber)
        js.put("name", name)
        js.put("room", room)
        js.put("school", school)
        response = Connection.sendJSON(getString(R.string.url) + "/reg/", js.toString())
        try {
            if (response[1] == "register_success") {
                Toast.makeText(this, "회원가입 성공.", Toast.LENGTH_SHORT).show()

                finish()
            } else if (response[1] == "register_failed:existent_account") {

                Toast.makeText(this, "이미 계정이 있습니다.", Toast.LENGTH_SHORT).show()
            } else if (response[1] == "register_failed:nonexistent_room") {

                Toast.makeText(this, "방이 없습니다.", Toast.LENGTH_SHORT).show()
            } else if (response[1] == "") {
                Toast.makeText(this, "오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

    }
}