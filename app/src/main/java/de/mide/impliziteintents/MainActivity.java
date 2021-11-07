package de.mide.impliziteintents;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * App demonstriert Verwendung von sog. <i>impliziten Intents</i>, mit
 * denen externe Apps geöffnet werden können.
 * Statt über die Angabe einer Ziel-Klasse wie bei expliziten Intents,
 * muss bei einem impliziten Intent zumindest eine sog. Action
 * angegeben werden, meist auch noch bestimmte Daten (z.B.
 * eine URL/URI, wenn "Anzeigen" als Action gewählt wurde).
 * <br><br>
 *
 * Für jede vorgestellte Intent-Art (die eine bestimmte andere
 * externe App öffnet) gibt es eine eigene Methode, die das
 * entsprechende Intent-Objekt zurückgibt, z.B.
 * {@link MainActivity#createIntentBrowserOeffnen()} oder
 * {@link MainActivity#createIntentGeoKoordinate()}.
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

    /**
     * Button um ein Foto aufzunehmen; es wird eine andere Activity aufgerufen
     * und nicht direkt ein impliziter Intent abgesetzt.
     */
    protected Button _fotoButton = null;


    /**
     * Lifecycle-Methode;
     * lädt Layout-Datei und weist den Buttons die Activity-Instanz selbst
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
     * <br><br>
     *
     * Wenn für einen Button festgestellt wird, dass der zugehörige
     * implizite Intent auf dem Android-Gerät nicht verarbeitet werden
     * kann, dann wird der Button deaktiviert.
     *
     * @param view  Button, der das Event ausgelöst hat.
     */
    @Override
    public void onClick(View view) {

        Intent intent = null;

        if (view == _browserButton) {

            intent = createIntentBrowserOeffnen();

        } else if (view == _geoKoordinateButton) {

            intent = createIntentGeoKoordinate();

        } else if (view == _appStoreButton) {

            intent = createIntentAppstoreEintrag();

        } else if (view == _emailButton) {

            intent = createIntentEMailVerfassen();

        } else if ( view == _telefonButton) {

            intent = createIntentTelefonanruf();

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


    /**
     * Liefert impliziten Intent für Anzeige einer bestimmte Webseite
     * in einer Browser-App zurück.
     *
     * @return Impliziter Intent zum Öffnen einer bestimmten URL
     *         in einer externen Browser-App.
     */
    protected Intent createIntentBrowserOeffnen() {

        Uri httpUri = Uri.parse("https://www.heise.de");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(httpUri);

        // Alternative: new Intent(Intent.ACTION_VIEW, httpUri);

        return intent;
    }


    /**
     * Liefert impliziten Intent für die Anzeige einer bestimmten
     * geographischen Koordinate (nämlich vom Karlsruher Schloss)
     * zurück; dieser Intent kann z.B. von den Apps "Google Maps"
     * oder "Maps.me" verarbeitet werden.
     *
     * @return Impliziter Intent zur Anzeige der geographischen
     *         Koordinate <i>49°1'Nord, 8°24'Ost</i>. Diese Koordinate
     *         ist rechts oben auf der Seite über Karlsruhe in der
     *         deutschsprachigen Wikipedia zu finden; wenn man
     *         dort auf diese Koordinate klickt, dann wird man
     *         zu der Seite "GeoHack - Karlsruhe" weitergeleitet,
     *         wo man die zugehörige Dezimalkoordinatendarstellung findet.
     */
    protected Intent createIntentGeoKoordinate() {

        // Dezimal-Koordinaten für Schloss KA als URI;
        // "Südlich" oder "Westlich" können mit negativen Vorzeichen definiert werden.
        Uri geoUri = Uri.parse("geo:49.014,8.4043");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoUri);

        return intent;
    }


    /**
     * Liefert impliziten Intent für die Anzeige einer
     * bestimmten App im App-Store-Client zurück,
     * nämlich <a href="http://bit.ly/2OFvBIB">Spiegel Online</a>.
     * Die in dieser URL enthaltene URL-Parameter "id" wird
     * auch für die URI benötigt.
     * <br><br>
     *
     * Der von dieser Methode erzeugte Intent kann u.U.
     * auch von den Client-Apps alternativer App-Stores
     * (z.B. F-Droid) verarbeitet werden, auch wenn diese
     * die App nicht finden.
     *
     * @return Impliziter Intent zur Anzeige der App
     *         von "Spiegel Online".
     */
    protected Intent createIntentAppstoreEintrag() {

        Uri appStoreUri = Uri.parse("market://details?id=de.spiegel.android.app.spon");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(appStoreUri);

        return intent;
    }


    /**
     * Liefert impliziten Intent für das Erstellen einer e-Mail zurück.
     * Es kann sein, dass sich neben eMail-Apps (z.B. GMail-App)
     * auch noch Instant-Messaging-Apps für diesen Intent zuständig
     * fühlen.
     *
     * @return Impliziter Intent zum Verfassen einer e-Mail,
     *         Betreff-Zeile und ein Inhalts-Satz sind schon
     *         vorgegeben.
     */
    protected Intent createIntentEMailVerfassen() {

        Uri emailUri = Uri.parse("mailto:");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(emailUri);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL   , new String[]{"info@eine-firma.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT , "Betreff-Zeile");
        intent.putExtra(Intent.EXTRA_TEXT    , "Hier steht die eigentliche Nachricht drin.");

        return intent;
    }


    /**
     * Liefert impliziten Intent für das Anwählen einer bestimmten
     * Telefon-Nummer zurück. Dieser Telefon-Anruf kann auch
     * vom im Android-SDK enthaltenen Emulator simuliert
     * werden.
     *
     * @return Impliziter Intent zum Anwählen der Telefonnummer <i>0123456789</i>.
     */
    protected Intent createIntentTelefonanruf() {

        Uri telefonUri = Uri.parse("tel:0123456789");

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(telefonUri);

        return intent;
    }

}
