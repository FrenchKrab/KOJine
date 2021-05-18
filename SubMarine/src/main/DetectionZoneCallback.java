package main;
import gameComponents.DetectionZone;

/*
 * Interface permettant un callback de la part d'une DetectionZone
 */
public interface DetectionZoneCallback {
	
	/**
	 * Appelé lorsque la zone entre en contact avec une autre
	 * @param ownZone La zone responsable de l'appel de fonction
	 * @param otherZone L'autre zone
	 */
	public abstract void ZoneEntered(DetectionZone ownZone, DetectionZone otherZone);
	
	
	/**
	 * Appelé lorsque la zone sort d'un contact avec une autre
	 * @param ownZone La zone responsable de l'appel de fonction
	 * @param otherZone L'autre zone
	 */
	public abstract void ZoneExited(DetectionZone ownZone, DetectionZone otherZone);
}
