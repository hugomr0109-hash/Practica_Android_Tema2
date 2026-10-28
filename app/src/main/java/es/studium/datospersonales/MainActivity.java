package es.studium.datospersonales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtNombre, txtApellidos, txtEdad;
    RadioGroup rgGenero;
    RadioButton rbHombre, rbMujer;
    Spinner spinnerEstadoCivil, spinnerIdioma;
    Switch switchHijos;
    TextView lblMensaje;
    Button btnMostrar, btnReiniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtEdad = findViewById(R.id.txtEdad);
        rgGenero = findViewById(R.id.rgGenero);
        rbHombre = findViewById(R.id.rbHombre);
        rbMujer = findViewById(R.id.rbMujer);
        spinnerEstadoCivil = findViewById(R.id.spinnerEstadoCivil);
        spinnerIdioma = findViewById(R.id.spinnerIdioma);
        switchHijos = findViewById(R.id.switchHijos);
        lblMensaje = findViewById(R.id.lblMensaje);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnReiniciar = findViewById(R.id.btnReiniciar);

        // Adaptadores para rellenar los desplegables (Spinners)
        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(
                this, R.array.opciones_estado_civil, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstadoCivil.setAdapter(adapterEstado);

        ArrayAdapter<CharSequence> adapterIdioma = ArrayAdapter.createFromResource(
                this, R.array.opciones_idiomas, android.R.layout.simple_spinner_item);
        adapterIdioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapterIdioma);

        // Configurar posición inicial del Spinner de idioma según el idioma actual
        String currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        if (currentLang.isEmpty()) {
            currentLang = Locale.getDefault().getLanguage();
        }

        int initialLangPosition = 0; // Español por defecto
        if (currentLang.startsWith("en")) {
            initialLangPosition = 1; // English
        } else if (currentLang.startsWith("fr")) {
            initialLangPosition = 2; // Français
        }
        spinnerIdioma.setSelection(initialLangPosition, false);

        // Listener para cambio de idioma
        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLangTag = "es";
                if (position == 1) {
                    selectedLangTag = "en";
                } else if (position == 2) {
                    selectedLangTag = "fr";
                }

                String curLang = AppCompatDelegate.getApplicationLocales().toLanguageTags();
                if (curLang.isEmpty()) {
                    curLang = Locale.getDefault().getLanguage();
                }

                if (!curLang.startsWith(selectedLangTag)) {
                    LocaleListCompat appLocales = LocaleListCompat.forLanguageTags(selectedLangTag);
                    AppCompatDelegate.setApplicationLocales(appLocales);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnMostrar.setOnClickListener(this);
        btnReiniciar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMostrar) {
            String nombre = txtNombre.getText().toString().trim();
            String apellidos = txtApellidos.getText().toString().trim();
            String edadStr = txtEdad.getText().toString().trim();

            // Validación específica acumulando únicamente los campos faltantes
            StringBuilder camposFaltantes = new StringBuilder();
            if (nombre.isEmpty()) {
                camposFaltantes.append(getString(R.string.val_nombre));
            }
            if (apellidos.isEmpty()) {
                if (camposFaltantes.length() > 0) camposFaltantes.append(", ");
                camposFaltantes.append(getString(R.string.val_apellidos));
            }
            if (edadStr.isEmpty()) {
                if (camposFaltantes.length() > 0) camposFaltantes.append(" y ");
                camposFaltantes.append(getString(R.string.val_edad));
            }

            // Si falta alguno, mostramos el aviso específico en rojo
            if (camposFaltantes.length() > 0) {
                lblMensaje.setTextColor(ContextCompat.getColor(this, R.color.error_rojo));
                lblMensaje.setText(getString(R.string.msg_falta_escribir, camposFaltantes.toString()));
            } else {
                // Si todo está correcto, limpiamos errores y generamos la frase de resultado
                lblMensaje.setText("");

                int edad = Integer.parseInt(edadStr);
                String mayorEdad = (edad >= 18) ? getString(R.string.mayor_edad) : getString(R.string.menor_edad);

                String genero = getString(R.string.genero_hombre);
                int idGeneroSeleccionado = rgGenero.getCheckedRadioButtonId();
                if (idGeneroSeleccionado == R.id.rbMujer) {
                    genero = getString(R.string.genero_mujer);
                }

                String estadoCivil = spinnerEstadoCivil.getSelectedItem().toString().toLowerCase();
                String hijos = switchHijos.isChecked() ? getString(R.string.con_hijos) : getString(R.string.sin_hijos);

                // Resultado final estructurado
                String resultado = apellidos + ", " + nombre + ", " + mayorEdad + ", " + genero + " " + estadoCivil + " y " + hijos + ".";
                lblMensaje.setTextColor(ContextCompat.getColor(this, R.color.black));
                lblMensaje.setText(resultado);
            }

        } else if (v.getId() == R.id.btnReiniciar) {
            // Reiniciar valores iniciales
            txtNombre.setText("");
            txtApellidos.setText("");
            txtEdad.setText("");
            rbHombre.setChecked(true);
            spinnerEstadoCivil.setSelection(0);
            switchHijos.setChecked(false);
            lblMensaje.setText("");
            txtNombre.requestFocus();
        }
    }
}