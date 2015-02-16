/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

/**
 * Factory for creating MailingEngines.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
public class MailingEngineFactory {

    private final static MailingEngineFactory INSTANCE = new MailingEngineFactory();

    // Factory pattern, get factory instance by getInstance().
    private MailingEngineFactory() {
    }

    /**
     * 
     * @return An instance of the factory.
     */
    public static MailingEngineFactory getInstance() {
        return INSTANCE;
    }

    /**
     * @return New or reused MailingEngine.
     */
    public MailingEngine getMailingEngine() {
        return new DefaultMailingEngine();
    }
}
