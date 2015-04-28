package snech.core.services;

import snech.core.types.User;

/**
 *
 * @author vanrado
 */
public interface IEmailService {
    public void sendInfoForTechnicianAssign(User user, long issueId);
    
    public void sendInfoForMemberAssign(User user, long issueId);
}
