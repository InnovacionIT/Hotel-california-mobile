package com.example.hotelcalifornia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hotelcaliforniaDatos.HabitacionDataAccess;
import com.example.hotelcaliforniaDatos.ReservaDataAccess;
import com.example.hotelcaliforniaModelo.Cliente;
import com.example.hotelcaliforniaModelo.Habitacion;
import com.example.hotelcaliforniaModelo.Reserva;
import com.example.hotelcaliforniaNegocio.GestorDeClientes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Home extends AppCompatActivity {
    EditText fechaIng, fechaSal; //para el calendario
    BottomNavigationView bottomNavigationView;
    Button reservarButton;
    ReservaDataAccess reservaDA;

    RadioGroup radioGroup;
    ArrayList<Habitacion> habitaciones; // Declaración de la lista de habitaciones

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        reservaDA = new ReservaDataAccess(this);
        reservarButton = findViewById(R.id.reservarButton);
        radioGroup = findViewById(R.id.RadioGroup);

        fechaIng = findViewById(R.id.fechaIng);
        fechaSal = findViewById(R.id.fechaSal);

        // Crear una instancia de HabitacionDataAccess
        HabitacionDataAccess habitacionDataAccess = new HabitacionDataAccess(this);

        // Obtener todas las habitaciones desde la base de datos y asignarlas a la variable miembro habitaciones
        habitaciones = habitacionDataAccess.getAll();
        // Inicializa bottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Configurar el texto de los RadioButtons con nombre y precio
        for (int i = 0; i < habitaciones.size(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            Habitacion habitacion = habitaciones.get(i);
            String textoRadioButton = habitacion.getHabTipo() + " - $ " + (int) habitacion.getHabPrecio();
            radioButton.setText(textoRadioButton);
        }
        // Configura el OnItemSelectedListener solo si bottomNavigationView no es nulo
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Tu código para manejar la navegación
                    return true;
                }
            });
        }


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override  //barra de navegación
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu) {
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.compra) {
                    Intent intent = new Intent(getApplicationContext(), Reservas.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.notificaciones) {
                    Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.perfil) {
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }
    public void fechaIngreso (View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog ing = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                fechaIng.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        // Establecer la fecha mínima permitida como HOY
        calendar.set(year, month, day);
        ing.getDatePicker().setMinDate(calendar.getTimeInMillis());

        ing.show();
    }

    public void fechaSalida (View view) {
        String fechaIngresoString = Utils.getStringFromEditText(fechaIng);

        if (!fechaIngresoString.isEmpty()) {
            String[] partesFecha = fechaIngresoString.split("/");
            int year = Integer.parseInt(partesFecha[2]);
            int month = Integer.parseInt(partesFecha[1]) - 1;
            int day = Integer.parseInt(partesFecha[0]);

            Calendar fechaIngresoCal = Calendar.getInstance();
            fechaIngresoCal.set(year, month, day);
            fechaIngresoCal.add(Calendar.DAY_OF_MONTH, 1);

            int egresoYear = fechaIngresoCal.get(Calendar.YEAR);
            int egresoMonth = fechaIngresoCal.get(Calendar.MONTH);
            int egresoDay = fechaIngresoCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog sal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int DayOfMonth) {
                    fechaSal.setText(DayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, egresoYear, egresoMonth, egresoDay);

            sal.getDatePicker().setMinDate(fechaIngresoCal.getTimeInMillis());
            sal.show();
        }
    }

    // TODO: Llevar el codigo de aca a GestorDeReservas adentro de un método
    //  crearReservaDe(int idHabitacion) para que sea más claro y legible el codigo
    //  y eliminar de aca el ReservaDataAccess y poner GestorDeReservas en su lugar
    public void reservar(View view){

        GestorDeClientes gestordeclientes =  new GestorDeClientes(this);
        Reserva reserva = new Reserva();

        SimpleDateFormat formato = new SimpleDateFormat(Utils.FORMATO_FECHA_FORMULARIO, Locale.getDefault());
        Date fechaIngreso = new Date();
        Date fechaEgreso = new Date();
        try {
            fechaIngreso = formato.parse(fechaIng.getText().toString());
            fechaEgreso = formato.parse(fechaSal.getText().toString());

        } catch (ParseException e) {

        }
        reserva.setCheckIn(fechaIngreso);
        reserva.setCheckOut(fechaEgreso);

        Cliente cliente = gestordeclientes.getClienteLogueado();
        reserva.setCliente(cliente);

        // Obtiene el ID de la habitación seleccionada desde el RadioGroup
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        // Verifica si se ha seleccionado un RadioButton válido
        if (selectedRadioButtonId != -1) {
            // Obtén la posición del RadioButton seleccionado en el grupo
            int selectedPosition = radioGroup.indexOfChild(findViewById(selectedRadioButtonId));

            // Verifica si la posición es válida y obtén la habitación correspondiente
            if (selectedPosition >= 0 && selectedPosition < habitaciones.size()) {
                Habitacion habitacionSeleccionada = habitaciones.get(selectedPosition);

                // Obtén la ID de la habitación seleccionada y configúrala en el objeto reserva
                int habitacionId = habitacionSeleccionada.getId();
                reserva.setHabitacion(habitacionSeleccionada);
            } else {
                // Manejar caso en el que no se ha seleccionado un RadioButton válido
            }
        } else {
            // Manejar caso en el que no se ha seleccionado un RadioButton
        }

        reserva.setAnulada(false);
        reserva.setPagada(false);
        reserva.setNotificadoAlCliente(false);

        reservaDA.create(reserva);

        Intent reservas = new Intent(this, Reservas.class);
        startActivity(reservas);
    } //lleva a reservas

}