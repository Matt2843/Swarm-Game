package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.swarmer.gui.StyleSheet;

public class ChatWidget extends Table {

	public static int chatWidgetCount = 1;

	private int widgetNo;

	protected float parentWidth, width, height;
	private float animationSpeed = 0.2f;

	protected Label interaction;
	private TextButton description;
	protected TextField input;

	private ScrollPane scrollList;
	protected Table scrollableObject;

	private boolean collapsed = true;

	protected ChatWidget(String title, int widgetNo) {
		this.parentWidth = Gdx.graphics.getWidth();
		this.width = parentWidth / 5;
		this.height = (float) (Gdx.graphics.getHeight() * 0.4);
		this.widgetNo = widgetNo;
		setSize(width, height);
		configureWidget(title);
		addWidgets();
	}

	private void configureWidget(String title) {
		Pixmap labelColor = new Pixmap((int)width, (int)height, Pixmap.Format.RGB888);
		labelColor.setColor(0.12f, 0.12f, 0.12f, 1);
		labelColor.fill();

		description = new TextButton(title, StyleSheet.defaultSkin);
		input = new TextField("", StyleSheet.defaultSkin);
		interaction = new Label("", StyleSheet.defaultSkin);
		scrollableObject = new Table();

		scrollList = new ScrollPane(scrollableObject, StyleSheet.defaultSkin);
		scrollList.setScrollingDisabled(true, false);
		scrollList.setFadeScrollBars(false);

		description.setSize(width, height / 12);
		scrollableObject.setSize(width, height * 10 / 12);
		input.setSize((float) (width * 0.8), height / 12);
		interaction.setSize((float) (width * 0.2), height / 12);
		interaction.setAlignment(Align.center);

		scrollableObject.setBackground(new Image(new Texture(labelColor)).getDrawable());
		scrollableObject.top();

		final MoveToAction collapse = new MoveToAction();
		collapse.setPosition(parentWidth - widgetNo * width, -(height - description.getHeight()));
		collapse.setDuration(animationSpeed);

		final MoveToAction expand = new MoveToAction();
		expand.setPosition(parentWidth - widgetNo * width, 0);
		expand.setDuration(animationSpeed);

		description.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				if(collapsed) {
					addAction(expand);
					collapse.reset();
				} else {
					addAction(collapse);
					expand.reset();
				}
				collapsed = !collapsed;			}
		});
		setPosition(parentWidth - widgetNo * width, -(height - description.getHeight()));
	}

	private void addWidgets() {
		defaults().width(width);
		add(description).colspan(2);
		row();
		add(scrollList).colspan(2);
		row();
		add(input).width(input.getWidth());
		add(interaction).width(interaction.getWidth());
	}


}
