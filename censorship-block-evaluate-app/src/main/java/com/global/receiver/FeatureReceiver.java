package com.global.receiver;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.global.dao.FeatureIndexDao;
import com.global.model.LinkFeature;

/**
 * The Class Receiver.
 */
@Component
public class FeatureReceiver {

	/** The latch. */
	private CountDownLatch latch = new CountDownLatch(1);
	
	@Autowired
	private FeatureIndexDao featureIndexDao;

	/**
	 * Receive message.
	 *
	 * @param message
	 *            the message
	 */
	public void receiveMessage(Object message) {
		try {
			processMessage(message);
			latch.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processMessage(Object message) throws Exception {
		try {
			LinkFeature linkFeature = (LinkFeature) SerializationUtils.deserialize((byte[]) message);
			if(linkFeature != null){
				featureIndexDao.insertFeature("censorship_feature_index", linkFeature);
			}
		} catch (Exception e) {
			System.err.println();
		}
	}

	/**
	 * Gets the latch.
	 *
	 * @return the latch
	 */
	public CountDownLatch getLatch() {
		return latch;
	}
}