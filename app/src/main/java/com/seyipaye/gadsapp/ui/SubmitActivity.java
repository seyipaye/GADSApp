package com.seyipaye.gadsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;

import com.seyipaye.gadsapp.R;
import com.seyipaye.gadsapp.databinding.ActivitySubmitBinding;
import com.seyipaye.gadsapp.databinding.DialogConfirmBinding;

public class SubmitActivity extends AppCompatActivity {

    private ActivitySubmitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySubmitBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.backBtn.setOnClickListener(view1 -> {
            finish();
        });

        binding.submitBtn.setOnClickListener(view1 -> {
            if (isValid()) {
                new ConfirmDialog(() -> {
                    AppRepository repository = AppRepository.getRepository();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    repository.sendForm(binding.fName.toString(), binding.lName.toString(), binding.email.toString(), binding.github.toString(), new Callbacks.Form() {
                        @Override
                        public void onSuccess(String message) {
                            binding.progressBar.setVisibility(View.GONE);
                            new SuccessDialog().show(getSupportFragmentManager(), "Dialog");
                        }

                        @Override
                        public void onFailure(String message) {
                            binding.progressBar.setVisibility(View.GONE);
                            new FailedDialog().show(getSupportFragmentManager(), "Dialog");
                        }
                    });
                }).show(getSupportFragmentManager(), "Dialog");

            }
        });
        setContentView(view);
    }

    private boolean isValid() {
        boolean valid = true;
        if (!TextUtils.isEmpty(binding.fName.getText().toString())) {
            binding.fName.setError(null);
        } else {
            valid = false;
            binding.fName.setError("Required");
        }

        if (!TextUtils.isEmpty(binding.lName.getText().toString())) {
            binding.lName.setError(null);
        } else {
            valid = false;
            binding.lName.setError("Required");
        }

        if (!TextUtils.isEmpty(binding.email.getText().toString())) {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                valid = false;
                binding.email.setError("Please enter a valid email address");
            } else {
                binding.email.setError(null);
            }
        } else {
            valid = false;
            binding.email.setError("Required");
        }

        if (!TextUtils.isEmpty(binding.github.getText().toString())) {
            if (!Patterns.WEB_URL.matcher(binding.github.getText().toString()).matches()) {
                valid = false;
                binding.github.setError("Please enter a valid url");
            } else {
                binding.github.setError(null);
            }
        } else {
            valid = false;
            binding.github.setError("Required");
        }

        return valid;
    }

    public static class ConfirmDialog extends DialogFragment {
        private DialogConfirmBinding binding;
        Callbacks.Yes listener;
        public ConfirmDialog(Callbacks.Yes listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            binding = DialogConfirmBinding
                    .inflate(LayoutInflater.from(getContext()));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(binding.getRoot());
            Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            binding.cancelButton.setOnClickListener(view -> dismiss());
            binding.yesBtn.setOnClickListener(view -> {
                dismiss();
                listener.onClicked();
            });
            return dialog;
        }
    }

    public static class SuccessDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(requireActivity().getLayoutInflater().inflate(R.layout.dialog_success, null));
            Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return dialog;
        }
    }

    public static class FailedDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(requireActivity().getLayoutInflater().inflate(R.layout.dialog_failed, null));
            Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return dialog;
        }
    }
}
