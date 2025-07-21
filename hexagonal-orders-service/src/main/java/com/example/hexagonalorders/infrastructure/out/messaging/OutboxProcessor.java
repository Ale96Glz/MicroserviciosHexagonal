package com.example.hexagonalorders.infrastructure.out.messaging;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Procesador que lee mensajes pendientes del outbox y los publica a Kafka.
 * Este componente garantiza la entrega confiable de mensajes procesando
 * mensajes del outbox de manera programada.
 */
@Component
public class OutboxProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(OutboxProcessor.class);
    
    private final OutboxRepository outboxRepository;
    private final MessagePublisher messagePublisher;
    
    public OutboxProcessor(OutboxRepository outboxRepository, MessagePublisher messagePublisher) {
        this.outboxRepository = outboxRepository;
        this.messagePublisher = messagePublisher;
    }
    
    /**
     * Procesa mensajes pendientes del outbox cada 20 segundos.
     * Este método se ejecuta automáticamente de manera programada.
     */
    @Scheduled(fixedDelay = 20000) // Ejecutar cada 20 segundos
    public void processPendingMessages() {
        try {
            List<OutboxMessage> pendingMessages = outboxRepository.findPendingMessages();
            
            if (!pendingMessages.isEmpty()) {
                logger.info("Procesando {} mensajes pendientes del outbox", pendingMessages.size());
                
                for (OutboxMessage message : pendingMessages) {
                    processMessage(message);
                }
            }
        } catch (Exception e) {
            logger.error("Error procesando mensajes del outbox", e);
        }
    }
    
    /**
     * Procesa un mensaje individual del outbox.
     * 
     * @param message el mensaje a procesar
     */
    private void processMessage(OutboxMessage message) {
        try {
            // Marcar como procesando
            message.markAsProcessing();
            outboxRepository.save(message);
            
            // Generar tópico dinámicamente
            String topic = message.getAggregateType() + "." + message.getEventType();
            
            logger.info("Procesando mensaje del outbox - Agregado: {}, Evento: {}, Tópico: {}", 
                message.getAggregateType(), message.getEventType(), topic);
            
            // Publicar mensaje a Kafka
            messagePublisher.publish(topic, message.getPayload());
            
            // Marcar como procesado
            message.markAsProcessed();
            outboxRepository.save(message);
            
        } catch (Exception e) {
            logger.error("Error procesando mensaje del outbox con id: {}", message.getId(), e);
            
            // Marcar como fallido
            message.markAsFailed(e.getMessage());
            outboxRepository.save(message);
        }
    }
} 