package lucassbeiler.aplicativo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import lucassbeiler.aplicativo.R;
import lucassbeiler.aplicativo.models.AlteracaoConta;
import lucassbeiler.aplicativo.utilitarias.CallsAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAlterarEmail extends AppCompatDialogFragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alterar_email, container, false);

        if(getDialog() != null){
            getDialog().setCancelable(true);
            getDialog().setCanceledOnTouchOutside(true);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Button botaoSalvar;

        final EditText etNovoEmail = view.findViewById(R.id.novo_email);
        botaoSalvar = view.findViewById(R.id.botao_salvar_bio);

        final SharedPreferences sharp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);

        etNovoEmail.setHint(sharp.getString("email", ""));
        botaoSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CallsAPI callsAPI = new CallsAPI();
                AlteracaoConta alteracaoConta = new AlteracaoConta(etNovoEmail.getText().toString(), "", "", "", "", null, null);
                callsAPI.retrofitBuilder().alteraConta(alteracaoConta, "Bearer " + sharp.getString("token", "")).enqueue(new Callback<AlteracaoConta>(){
                    @Override
                    public void onResponse(Call<AlteracaoConta> call, Response<AlteracaoConta> resposta){
                        if(resposta.isSuccessful() && !etNovoEmail.getText().toString().isEmpty()){
                            TastyToast.makeText(getActivity(), "E-mail atualizado com sucesso!", Toast.LENGTH_LONG, TastyToast.SUCCESS);
                            dismiss();
                        }else{
                            try{
                                TastyToast.makeText(getActivity(), new JSONObject(resposta.errorBody().string()).getString("error"), Toast.LENGTH_LONG, TastyToast.ERROR);
                            }catch(Exception e){
                                Log.d("EDITACONTA EXCEPTION", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AlteracaoConta> call, Throwable t){
                        Log.d("EDITACONTA EXCEPTION2", t.getMessage());
                    }
                });
            }
        });

    }


}