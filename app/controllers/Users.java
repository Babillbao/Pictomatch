package controllers;

import java.lang.reflect.Constructor;

import play.data.binding.Binder;
import play.db.Model;

public class Users extends CRUD {

	public static void register() throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        Binder.bindBean(params.getRootParamNode(), "object", object);
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            render(type, object);
        }
        object._save();
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        render();
    }
}