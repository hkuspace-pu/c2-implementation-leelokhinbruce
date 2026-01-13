package com.example.guestreservationapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.databinding.ActivityCompleteProfileBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CompleteProfileActivity extends BaseValidatedActivity {
    private ActivityCompleteProfileBinding binding;
    private String firstName, lastName, phoneNumber;
    private String selectedCountryCode = "+44";  // default
    private boolean isValidPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.editFirstName.addTextChangedListener(inputFieldWatcher);
        binding.editLastName.addTextChangedListener(inputFieldWatcher);
        binding.editPhone.addTextChangedListener(inputFieldWatcher);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.country_codes,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCountryCode.setAdapter(adapter);

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Select country code
        binding.spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedCountryCode = adapterView.getItemAtPosition(pos).toString();
                if (phoneNumber != null)
                    // re-validate when code changes
                    isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCountryCode = "+44";
            }
        });

        // Complete button click
        binding.btnComplete.setEnabled(false);
        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompleteProfileActivity.this, "Create Account and Profile successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            firstName = binding.textInputFirstName.getEditText().getText().toString().trim();
            lastName = binding.textInputLastName.getEditText().getText().toString().trim();
            phoneNumber = binding.editPhone.getText().toString().trim();

            boolean firstNameNotEmpty = isNotFieldEmpty(firstName, binding.textInputFirstName, "Please enter your first name");
            boolean lastNameNotEmpty = isNotFieldEmpty(lastName, binding.textInputLastName, "Please enter your last name");
            isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);

            binding.btnComplete.setEnabled(firstNameNotEmpty && lastNameNotEmpty && isValidPhone);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };
}