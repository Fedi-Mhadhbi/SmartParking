import * as functions from "firebase-functions/v1";
import {FieldValue} from "firebase-admin/firestore";

import {db} from "../shared/admin";

export const onUserCreate = functions.auth.user().onCreate(async (user) => {
  await db.collection("users").doc(user.uid).set({
    uid: user.uid,
    fullName: user.displayName ?? "",
    email: user.email ?? "",
    phone: user.phoneNumber ?? "",
    roles: ["DRIVER"],
    activeMode: "DRIVER",
    profileImage: user.photoURL ?? "",
    createdAt: FieldValue.serverTimestamp(),
    isActive: true,
  });

  functions.logger.info("Created user profile", {uid: user.uid});
});
