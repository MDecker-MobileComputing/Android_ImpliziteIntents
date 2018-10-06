package de.mide.impliziteintents;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;


/**
 * Diese Klasse beinhaltet Hilfsmethoden, die von beiden Activity-Klassen
 * verwendet werden.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class HilfsKlasse {

    /** Tag-String für Log-Nachrichten. */
    public static final String TAG4LOGGING = "ImpliziteIntents";


    /**
     * Convenience-Methode, um Nachricht <i>nachricht</i> mit
     * einem Toast-Objekt darzustellen.
     *
     * @param context Referenz auf aufrufende Activity
     *
     * @param nachricht Auszugebende Nachricht
     */
    protected static void zeigeToast(Context context, String nachricht) {
        Toast.makeText(context, nachricht, Toast.LENGTH_LONG).show();
    }


    /**
     * Die Methode überprüft, ob es für den als Parameter übergebenen Intent eine
     * passende Zielkomponente auf dem Android-Gerät gibt.
     * Wenn es keine Komponente gibt, dann wird eine Fehlermeldung
     * als Toast ausgegeben.<br/><br/>
     *
     * Wenn diese Methode für einen Intent <tt>false</tt> liefert,
     * der Intent aber trotzdem mit <i>startActivity(intent)</i>
     * abgesetzt wird, dann crasht die App.<br/><br/>
     *
     * Die Verwendung der Methode {@link Intent#resolveActivity(PackageManager)}
     * wird auch auf
     * <a href="https://developer.android.com/guide/components/intents-common.html">dieser Seite</a>
     * im API-Guide beschrieben. Wenn es mehrere Apps gibt, die den Intent
     * verarbeiten/empfangen können, dann wird die mit der höchsten Priorität
     * zurückgeliefert.
     *
     * @param context Referenz auf aufrufende Activity
     *
     * @param intent Intent-Objekt (impliziter Intent), für das überprüft werden soll,
     *               ob auf dem aktuellen Android-Gerät es mindestens eine App gibt,
     *               den den Intent verarbeiten kann.
     *
     * @return <tt>true</tt>, wenn es mindestens einen Receiver für
     *         den <i>intent</i> gibt.
     */
    public static boolean wirdIntentUnterstuetzt(Context context, Intent intent) {

        PackageManager packageManager = context.getPackageManager();

        ComponentName componentName = intent.resolveActivity(packageManager);

        if (componentName == null) {

            Log.w(TAG4LOGGING,
                  "Nicht-unterstützter Intent: ACTION=" + intent.getAction() +
                  ", DATA=" + intent.getDataString() );

            return false;
        } else {
            return true;
        }
    }

}
