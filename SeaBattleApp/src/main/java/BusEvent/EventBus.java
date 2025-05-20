/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusEvent;

/**
 *
 * @author Ruben
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {

    // Lista de subscriptores segura para múltiples hilos
    private static final List<Subscriber<?>> subscriptores = new CopyOnWriteArrayList<>();

    /**
     * Suscribe un listener a un tipo específico de evento. Devuelve una
     * Subscription que permite cancelar la suscripción.
     */
    public static <T extends Evento> Subscription suscribir(Class<T> tipoEvento, Consumer<T> listener) {
        Subscriber<T> sub = new Subscriber<>(tipoEvento, listener);
        subscriptores.add(sub);
        return () -> subscriptores.remove(sub);
    }

    /**
     * Publica un evento a todos los subscriptores correspondientes.
     */
    public static void publicar(Evento evento) {
        for (Subscriber<?> sub : subscriptores) {
            sub.entregar(evento);
        }
    }

    /**
     * Interfaz que representa una suscripción cancelable.
     */
    public interface Subscription {

        void cancel();
    }

    /**
     * Clase interna que representa un suscriptor genérico.
     */
    private static class Subscriber<T extends Evento> {

        private final Class<T> tipoEvento;
        private final Consumer<T> listener;

        public Subscriber(Class<T> tipoEvento, Consumer<T> listener) {
            this.tipoEvento = tipoEvento;
            this.listener = listener;
        }

        public void entregar(Evento evento) {
            if (tipoEvento.isInstance(evento)) {
                listener.accept(tipoEvento.cast(evento));
            }
        }
    }
}
