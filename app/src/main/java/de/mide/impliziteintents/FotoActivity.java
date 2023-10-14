package de.mide.impliziteintents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Activity zur Aufnahme eines Fotos über die Kamera-App;
 * demonstriert Callback-Methode für implizite Intents.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class FotoActivity extends Activity
                          implements View.OnClickListener  {

    /** Button für impliziten Intent zum Aufruf Kamera-App. */
    protected Button _fotoAusloesenButton = null;

    /** Button um zur MainActivity zurückzukehren. */
    protected Button _zurueckButton = null;

    /** UI-Element zur Anzeige des aufgenommenen Fotos. */
    protected ImageView _fotoImageView = null;


    /**
     * Lifecycle-Methode. Lädt Layout-Datei und setzt Activity-Instanz
     * selbst als Event-Handler-Objekt für die beiden Buttons; es werden.
     * außerdem die Member-Variablen, die UI-Elemente repräsentieren,
     * gefüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        _fotoAusloesenButton = findViewById(R.id.fotoAusloeserButton);
        _fotoAusloesenButton.setOnClickListener(this);

        _zurueckButton = findViewById(R.id.zurueckButton);
        _zurueckButton.setOnClickListener(this);

        _fotoImageView = findViewById(R.id.fotoImageView);
    }


    /**
     * Event-Handler für Buttons.
     *
     * @param view Button, der das Event ausgelöst hat.
     */
    @Override
    public void onClick(View view) {

        if (view == _fotoAusloesenButton) {

            kameraAufrufen();

        } else if (view == _zurueckButton) {

            finish();

        } else {

            Log.e(HilfsKlasse.TAG4LOGGING,
                    "Unerwartetes View-Objekt in Event-Handler für Buttons: " + view );

            HilfsKlasse.zeigeToast(this,
                    "INTERNER FEHLER: Event-Handler von unerwartetem UI-Element ausgelöst.");
        }
    }


    /**
     * Ruft Kamera-App mit einem impliziten Intent auf.
     * Benötigte Action {@link MediaStore#ACTION_IMAGE_CAPTURE}.
     * Wenn die so aufgerufene Kamera-App beendet wird, dann
     * wird zur Benachrichtigung dieser Activity die Callback-Methode
     * {@link FotoActivity#onActivityResult(int, int, Intent)} aufgerufen
     * <br><br>
     *
     * Da kein vollqualifizierter Zieldateiname als Uri-Objekt mit
     * dem Key {@link MediaStore#EXTRA_OUTPUT} mitgegeben wird,
     * enthält das der Callback-Methode übergebene Intent-Objekt
     * das aufgenommene Bild in verkleinerter Form als Objekt der
     * Klasse {@link Bitmap} unter dem Key "data".
     */
    protected void kameraAufrufen() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if ( HilfsKlasse.wirdIntentUnterstuetzt(this, intent) == false) {

            HilfsKlasse.zeigeToast(this, "Auf diesem Gerät können keine Foto aufgenommen werden.");
            _fotoAusloesenButton.setEnabled(false);
            return;
        }

        startActivityForResult(intent, 4242); // 4242=RequestCode

        Log.i(HilfsKlasse.TAG4LOGGING, "Intent an Kamera abgeschickt.");
    }


    /**
     * Callback-Methode für Intent: Kamera-App ist beendet worden.
     *
     * @param requestCode Erkennungs-Code für beendete Activity; wird hier nicht
     *                    ausgewertet, da die Activity nur einen Intent
     *                    absetzen kann, nämlich impliziten Intent zum Aufruf
     *                    der Kamera-App.
     *
     * @param resultCode  Ergebnis-Code zur Erkennung, ob Intent erfolgreich ausgeführt
     *                    oder abgebrochen (z.B. Foto verworfen).
     *
     * @param intent Wenn Kamera-App ohne Angabe eines Zieldateinamens für die
     *               Foto-Datei aufgerufen wird, dann enthält dieses Intent-Objekt
     *               unter dem Schlüssel "data" ein Extra-Objekt vom Typ {@link Bitmap},
     *               in dem das aufgenommene Foto (verkleinert) enthalten ist.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode != RESULT_OK) {

            HilfsKlasse.zeigeToast(this, "Kein Foto aufgenommen.");
            return;
        }

        if (intent.hasExtra("data") == false) {

            HilfsKlasse.zeigeToast(this, "Kein Bitmap-Objekt von Kamera-App erhalten.");
            return;
        }

        Bitmap fotoBitmap = intent.getParcelableExtra("data");
        Log.i(HilfsKlasse.TAG4LOGGING,
                "Bitmap erhalten: " + fotoBitmap.getWidth() + "x" + fotoBitmap.getHeight() );

        _fotoImageView.setImageBitmap(fotoBitmap);
    }

}
