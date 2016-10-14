package com.pb.client.sdk.handler;

import com.pb.server.model.Message;
import com.pb.server.session.PBSession;

/**
 * Created by piecebook on 2016/10/9.
 */
public interface PBIOHandler {
    public abstract Message process(PBSession session, Message msg);
}
