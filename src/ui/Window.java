package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.FileUtil;
import card.Pokemon;
import card.Type;

public class Window{
	
	private static JPanel cards;
	private static JFrame frame = new JFrame();
	private FileUtil fileUtil;
    private String name;
    private final int tailleX = 500;
    private final int tailleY = 500;
	
	public Window(String name){
        this.name = name;
        this.fileUtil = new FileUtil("data.txt", this);
        this.frame.setTitle(name);
        this.frame.setSize (tailleX , tailleX);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.addToFrame();
        
	}
	
	private class ChangeCard implements ActionListener {

	    private String name, title;
	    private int x = tailleX, y = tailleY;
	    private ChangeCard(String name, String title) {
	        this.name = name;
	        this.title = title;
	    }
	    private ChangeCard(String name, String title, int x, int y) {
	        this.name = name;
	        this.title = title;
	        this.x = x;
	        this.y = y;
	    }
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        frame.setTitle(title);
	        frame.setSize (this.x , this.y);
	    	itemChanged(name);
	    }

	}
	
	private void addToFrame(){
		JPanel accueil = new JPanel();
		accueil.setLayout(new GridLayout(4, 3));
		
		JButton buttonAddPokemon = new JButton("Ajouter un pokemon");
		buttonAddPokemon.addActionListener(new ChangeCard("addPokemon", "Ajouter un pokemon"));
		accueil.add(buttonAddPokemon);
		
		JButton buttonViewPokemon = new JButton("Voir les pokemon");
		buttonViewPokemon.addActionListener(new ChangeCard("viewPokemon", "Les pokemon"));
		accueil.add(buttonViewPokemon);
		
		JButton addPokemonType = new JButton("Ajouter un type de pokemon");
		addPokemonType.addActionListener(new ChangeCard("typePokemon", "Ajouter un type de pokemon", 700, 500));
		accueil.add(addPokemonType);
		
		JButton addPokemonAbilitie = new JButton("Ajouter une capacité de pokemon");
		addPokemonAbilitie.addActionListener(new ChangeCard("abilitiePokemon", "Ajouter une capacité de pokemon"));
		accueil.add(addPokemonAbilitie);
		
        this.cards = new JPanel(new CardLayout());
        this.cards.add(accueil, "accueil");
        this.cards.add(getTemplateAddPokemon(), "addPokemon");
        this.cards.add(getTemplateViewPokemon(), "viewPokemon");
        this.cards.add(getTemplateTypePokemon(), "typePokemon");
        
        this.frame.add(cards);
		
	}
	
	private JPanel getTemplateAddPokemon(){
		
		JPanel template = new JPanel();
		template.setLayout(new GridLayout(6, 2));

		Panel nameP = new Panel();
		nameP.add(new JLabel("Nom :"));
		JTextField name = new JTextField(20);
		nameP.add(name);
		template.add(nameP);
	    
		Panel hpP = new Panel();
		hpP.add(new JLabel("Point de vie :"));
		JTextField hp = new JTextField(4);
		hpP.add(hp);
		template.add(hpP);
		
		Panel typeP = new Panel();
		typeP.add(new JLabel("Type :"));
		JComboBox<Object> combo = new JComboBox<Object>();
		combo.setPreferredSize(new Dimension(100, 20));
		for (Type type : Type.pokemonType) {
		    combo.addItem(type);
		}
		typeP.add(combo);
		template.add(typeP);
	    
	    Panel stageP = new Panel();
	    stageP.add(new JLabel("Evolution :"));
	    JComboBox<Object> comboStage = new JComboBox<Object>();
	    comboStage.setPreferredSize(new Dimension(100, 20));
	    comboStage.addItem(1);
	    comboStage.addItem(2);
	    comboStage.addItem(3);
	    stageP.add(comboStage);
	    template.add(stageP);
	    
	    JButton submit = new JButton("Ajouter");
	    submit.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	String pokemonName = name.getText();
	        	String hpText = hp.getText();
	        	Integer pokemonHp = Integer.parseInt(hpText);
	        	Type pokemonType = (Type)combo.getSelectedItem();
	        	Integer pokemonStage = (Integer)comboStage.getSelectedItem();
	        	fileUtil.write(new Pokemon(pokemonName, pokemonHp ,pokemonType, pokemonStage, null));
	        	
		        frame.setTitle("Accueil");
		    	itemChanged("accueil");
	        }
	    });
	    
	    template.add(submit);

		JButton home = new JButton("Accueil");
		home.addActionListener(new ChangeCard("accueil", "Accueil"));
		template.add(home);
	
		return template;
	}
	
	private JPanel getTemplateViewPokemon(){
		
		JPanel template = new JPanel();
		template.setLayout(new GridLayout(6, 2));
		
		List<Pokemon> pokemons = fileUtil.getPokemon();
		
		String firstName = "";
		String firstHp = "";
		String firstStage = "";
		String firstType = "";
		
		if(!pokemons.isEmpty()){
			Pokemon firstPokemon = pokemons.get(0);
			
			firstName = firstPokemon.getPokemonName();
			firstHp = String.valueOf(firstPokemon.getHp());
			firstStage = String.valueOf(firstPokemon.getStage());
			firstType = firstPokemon.getType().getTypeName();
		}
		
		JLabel name = new JLabel(firstName);
		JLabel hp = new JLabel(firstHp);
		JLabel stage = new JLabel(firstStage);
		JLabel type = new JLabel(firstType);

		Panel dropdownP = new Panel();
		dropdownP.add(new JLabel("Pokemon :"));
		JComboBox<Object> combo = new JComboBox<Object>();
		combo.setPreferredSize(new Dimension(100, 20));
		for (Pokemon pokemon : pokemons) {
		    combo.addItem(pokemon);
		}
		
		combo.addItemListener(new ItemListener () {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
			       Pokemon item = (Pokemon) e.getItem();
			       name.setText(item.getPokemonName());
			       hp.setText(String.valueOf(item.getHp()));
				   stage.setText(String.valueOf(item.getStage()));
				   type.setText(item.getType().getTypeName());
			    }
			}
		});
		dropdownP.add(combo);
		template.add(dropdownP);
	    
	    Panel nameP = new Panel();
	    nameP.add(new JLabel("Nom :"));
	    nameP.add(name);
	    template.add(nameP);
		
	    Panel hpP = new Panel();
	    hpP.add(new JLabel("Hp :"));
	    hpP.add(hp);
	    template.add(hpP);
		
	    Panel stageP = new Panel();
	    stageP.add(new JLabel("Stage :"));
	    stageP.add(stage);
	    template.add(stageP);

	    Panel typeP = new Panel();
	    typeP.add(new JLabel("Type :"));
	    typeP.add(type);
	    template.add(typeP);
	    
	    JButton home = new JButton("Accueil");
		home.addActionListener(new ChangeCard("accueil", "Accueil"));
		template.add(home);
		
		return template;
	}
	
	private JPanel getTemplateTypePokemon(){
		
		JPanel template = new JPanel();
		template.setLayout(new BorderLayout());
		
		Panel north = new Panel(); 
		north.add(new JLabel("Nom du type :"));
		JTextField name = new JTextField(20);
		north.add(name);
		template.add("North", north);
	    
	    JLabel banner = new JLabel("Welcome to the Tutorial Zone!", JLabel.CENTER);
		banner.setForeground(Color.yellow);
		JColorChooser colorPicker = new JColorChooser(banner.getForeground());
	    template.add(colorPicker);
	
	    JButton home = new JButton("Accueil");
		home.addActionListener(new ChangeCard("accueil", "Accueil"));
		template.add("South", home);
		
		return template;
	}
	
	public void pokemonUpdate(){
        this.cards.removeAll();
        this.cards.repaint();
        this.addToFrame();
	}
	
	public void itemChanged(String name) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, name);
    }
	
	public void display(){
        frame.setVisible(true);
	}
}
