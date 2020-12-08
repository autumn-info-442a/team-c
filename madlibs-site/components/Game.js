import React, {Component} from 'react';
import Lobby from './Lobby';
import Prompt from './Prompt';
import Result from './Result';

class Game extends Component {

  constructor(props) {
    super(props);
    this.state = {
      start: false,
      done: false,
    }
  }

  handleStart = (e) => {
    this.setState({ start: true })
  }

  handleReplay = (e) => {
    this.setState({ start: false })
    this.setState({ done: false })
  }

  handleDone = (e) => {
    //temp
    if (e.key === 'Enter') {
      this.setState({ done: true })
    }
  }

  render() {
    return(
      <div>
        {this.state.start ?
          [
            this.state.done ?
            <Result handleReplay={this.handleReplay.bind(this)}></Result>
            :
            <Prompt handleDone={this.handleDone.bind(this)}></Prompt>
          ]
          :
          <Lobby handleStart={this.handleStart.bind(this)}></Lobby>
        }
      </div>
    );
  }
}

export default Game