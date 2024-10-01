/*
 * Result from running an action
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils.events;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public enum ActionType {
    STOP_NOW,
    FAILED,
    OK,
    WARNING,
    NOT_YET_STARTED,
    NOTHING
}
