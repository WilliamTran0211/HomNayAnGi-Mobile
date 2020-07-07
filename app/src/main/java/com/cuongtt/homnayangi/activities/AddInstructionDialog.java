package com.cuongtt.homnayangi.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cuongtt.homnayangi.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class AddInstructionDialog extends AppCompatDialogFragment {

    OnMyDialogResult mDialogResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout_add_instructions, null);

        final TextInputEditText edtInstruction = view.findViewById(R.id.edt_instruction);

        builder.setView(view).setTitle("Thêm bước")
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String instructions = edtInstruction.getText().toString();

                        if( mDialogResult != null ){
                            System.out.println(instructions);
                            mDialogResult.finish(instructions);
                        }
                        AddInstructionDialog.this.dismiss();
                    }
                });

        return builder.create();
    }

    public void setDialogResult(AddInstructionDialog.OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String result);
    }
}
