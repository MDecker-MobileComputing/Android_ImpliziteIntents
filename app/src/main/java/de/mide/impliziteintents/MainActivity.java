package de.mide.impliziteintents;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * App demonstriert Verwendung von sog. <i>impliziten Intents</i>, mit
 * denen externe Apps geöffnet werden können.
 * Statt über die Angabe einer Ziel-Klasse wie bei expliziten Intents
 * muss bei einem impliziten Intent zumindest eine sog. Action
 * angegeben werden, meist auch noch bestimmte Daten (z.B.
 * eine URL/URI, wenn "Anzeigen" als Action gewählt wurde).
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends Activity
                          implements View.OnClickListener {


    /** Button um Web-Browser zur Anzeige einer bestimmten URL zu öffnen. */
    protected Button _browserButton = null;

    /** Button um externe App zur Anzeige einer Geo-Koordinate zu öffnen. */
    protected Button _geoKoordinateButton = null;

    /** Button um bestimmten Eintrag im App-Store-Client anzuzeigen. */
    protected Button _appStoreButton = null;

    /** Buttom um e-Mail-App zu öffnen. */
    protected Button _emailButton = null;

    /** Button um eine Telefonnummer anzuwählen. */
    protected Button _telefonButton = null;

    /** Button um ein Foto aufzunehmen; es wird eine andere Activity aufgerufen
     *  und nicht direkt ein impliziter Intent abgesetzt. */
    protected Button _fotoButton = null;


    /**
     * Lifecycle-Methode.
     * Lädt Layout-Datei und weist den Buttons die Activity-Instanz selbst
     * als Event-Handler-Objekt zu. Es werden auch die Member-Variablen
     * für die UI-Elemente befüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _browserButton = findViewById(R.id.browserButton);
        _browserButton.setOnClickListener(this);

        _geoKoordinateButton = findViewById(R.id.geoKoordinateButton);
        _geoKoordinateButton.setOnClickListener(this);

        _appStoreButton = findViewById(R.id.appStoreButton);
        _appStoreButton.setOnClickListener(this);

        _emailButton = findViewById(R.id.emailButton);
        _emailButton.setOnClickListener(this);

        _telefonButton = findViewById(R.id.telefonButton);
        _telefonButton.setOnClickListener(this);

        _fotoButton = findViewById(R.id.fotoButton);
        _fotoButton.setOnClickListener(this);
    }


    /**
     * Event-Handler für ALLE Buttons dieser Activity.
     * <br/><br/>
     *
     * Wenn für einen Button festgestellt wird, dass der zugehörige
     * implizite Intent auf dem Android-Gerät nicht verarbeitet werden
     * kann, dann wird der Button deaktiviert.
     *
     * @param view Button, der das Event ausgelöst hat.
     */
    @Override
    public void onClick(View view) {

        Intent intent = null;

        if (view == _browserButton) {

            Uri httpUri = Uri.parse("https://www.heise.de");

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(httpUri);

            // Verwendung anderer Konstruktor von Klasse Intent:
            // new Intent(Intent.ACTION_VIEW, uri);

        } else if (view == _geoKoordinateButton) {

            // Dezimal-Koordinaten für Schloss KA als URI;
            // "Südlich" oder "Westlich" können mit negativen Vorzeichen definiert werden)
            Uri geoUri = Uri.parse("geo:49.014,8.4043");

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoUri);

        } else if (view == _appStoreButton) {

            // Paket-Bezeichner für App "Spiegel-Online (SPON)", siehe Wert für Parameter "id" von
            // URL in Google Play: https://play.google.com/store/apps/details?id=de.spiegel.android.app.spon
            // Möglichkeiten mit "market://": http://developer.android.com/distribute/tools/promote/linking.html
            Uri appStoreUri = Uri.parse("market://details?id=de.spiegel.android.app.spon");

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(appStoreUri);

        } else if (view == _emailButton) {

            Uri emailUri = Uri.parse("mailto:");

            intent = new Intent(Intent.ACTION_SEND);
            intent.setData(emailUri);

            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Betreff-Zeile");
            intent.putExtra(Intent.EXTRA_TEXT, "Hier steht die eigentliche Nachricht drin.");

        } else if ( view == _telefonButton) {

            Uri telefonUri = Uri.parse("tel:0123456789");

            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(telefonUri);

        } else if ( view == _fotoButton) {

            intent = new Intent(this, FotoActivity.class);
            startActivity(intent);
            return;

        } else {

            HilfsKlasse.zeigeToast(this,
                    "INTERNER FEHLER: Event-Handler von unerwartetem UI-Element ausgelöst.");
            Log.e(HilfsKlasse.TAG4LOGGING,
                  "Unerwartetes View-Objekt in Event-Handler für Buttons: " + view );

            return; // Abbruch
        }


        // Der implizite Intent darf nur dann abgeschickt werden, wenn es auf dem
        // Android-Gerät mindestens eine App gibt, die den Intent unterstützt;
        // ist dies nicht der Fall und das Intent-Objekt wird trotzdem abgeschickt,
        // dann stürzt die App ab.
        if ( HilfsKlasse.wirdIntentUnterstuetzt(this, intent) ) {
            startActivity(intent);
        }
        else {
            HilfsKlasse.zeigeToast(this,
                    "Dieser Intent wird auf Ihrem Gerät leider nicht unterstützt.");
            view.setEnabled(false); // Button deaktivieren
        }
    }


}
